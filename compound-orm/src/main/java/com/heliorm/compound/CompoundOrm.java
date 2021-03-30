package com.heliorm.compound;

import com.heliorm.*;
import com.heliorm.def.Executable;
import com.heliorm.def.Select;
import com.heliorm.impl.Part;
import com.heliorm.impl.Selector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class CompoundOrm implements Orm {

    private final List<Orm> orms;
    private final Map<Class<?>, Optional<Orm>> typeToOrm = new ConcurrentHashMap();

    public CompoundOrm(Orm... orms) {
        this.orms = Arrays.asList(orms);
    }

    @Override
    public <O> O create(O o) throws OrmException {
        return ormFor(o).create(o);
    }

    @Override
    public <O> O update(O o) throws OrmException {
        return ormFor(o).update(o);
    }

    @Override
    public <O> void delete(O o) throws OrmException {
        ormFor(o).delete(o);
    }

    @Override
    public <T extends Table<O>, O> Select<T, O, T, O> select(T t) {
        try {
            return ormFor(t.getObjectClass()).select(t);
        }
        catch (OrmException ex) {
            throw new UncaughtOrmException(ex.getMessage(), ex);
        }
    }

    @Override
    public OrmTransaction openTransaction() throws OrmException {

        List<OrmTransaction> transactions = orms.stream().map(orm -> {
            try {
               return orm.openTransaction();
            }
            catch (OrmException ex) {
                throw new UncaughtOrmException(ex.getMessage(), ex);
            }
        }).collect(Collectors.toList());

        return new OrmTransaction() {
            @Override
            public void commit() throws OrmException {
                for (OrmTransaction tx : transactions){
                    tx.commit();
                }
            }

            @Override
            public void rollback() throws OrmException {
                for (OrmTransaction tx : transactions){
                    tx.rollback();
                }

            }

            @Override
            public void close() throws OrmException {
                for (OrmTransaction tx : transactions){
                    tx.close();
                }
            }
        };
    }

    @Override
    public void close() {
        for (Orm orm : orms) {
            orm.close();
        }
    }

    @Override
    public <O> Table<O> tableFor(O o) throws OrmException {
        return ormFor(o).tableFor(o);
    }

    @Override
    public <O> Table<O> tableFor(Class<O> aClass) throws OrmException {
        return ormFor(aClass).tableFor(aClass);
    }

    @Override
    public Selector selector() {
        return new Selector() {
            @Override
            public <O, P extends Part & Executable> List<O> list(P p) throws OrmException {
                return ormFor(p.getReturnTable()).selector().list(p);
            }

            @Override
            public <O, P extends Part & Executable> Stream<O> stream(P p) throws OrmException {
                return ormFor(p.getReturnTable()).selector().stream(p);
            }

            @Override
            public <O, P extends Part & Executable> Optional<O> optional(P p) throws OrmException {
                return ormFor(p.getReturnTable()).selector().optional(p);
            }

            @Override
            public <O, P extends Part & Executable> O one(P p) throws OrmException {
                return ormFor(p.getReturnTable()).selector().one(p);
            }
        };
    }

    private <O> Orm ormFor(O o) throws OrmException {
        return ormFor(o.getClass());
    }

    private <O> Orm ormFor(Class<O> type) throws OrmException {
        Optional<Orm> found = typeToOrm.computeIfAbsent(type, key -> {
            for (Orm orm : orms) {
                try {
                    orm.tableFor(key);
                    return Optional.of(orm);
                } catch (OrmException ex) {
                }
            }
            return Optional.empty();
        });
        if (!found.isPresent()) {
            throw new OrmException(format("Cannot find ORM for objects of type %s", type.getSimpleName()));
        }
        return found.get();
    }

}
